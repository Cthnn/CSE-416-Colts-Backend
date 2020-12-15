package com.example.demo;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import com.example.demo.EnumClasses.JobStatus;
import com.example.demo.PersistenceClasses.Job;
import com.example.demo.WrapperClasses.AlgorithmInputs;
import com.example.demo.WrapperClasses.PathBuilder;
import com.example.demo.Repositories.JobRepository;
public class ServerDispatcher {
    private static int thresh = 20;
    private static Map<String,JobStatus> statusMapping= Map.ofEntries(Map.entry("BOOT_FAIL",JobStatus.ABORTED),
        Map.entry("CANCELLED",JobStatus.ABORTED),Map.entry("COMPLETED",JobStatus.COMPLETED),Map.entry("CONFIGURING",JobStatus.QUEUED),
        Map.entry("COMPLETING",JobStatus.INPROGRESS),Map.entry("DEADLINE",JobStatus.ABORTED),Map.entry("FAILED",JobStatus.ABORTED),
        Map.entry("NODE_FAIL",JobStatus.ABORTED),Map.entry("OUT_OF_MEMORY",JobStatus.ABORTED),Map.entry("PENDING",JobStatus.QUEUED),
        Map.entry("PREEMPTED",JobStatus.ABORTED),Map.entry("RUNNING",JobStatus.INPROGRESS),Map.entry("RESV_DEL_HOLD",JobStatus.INPROGRESS),
        Map.entry("REQUEUE_FED",JobStatus.INPROGRESS),Map.entry("REQUEUED",JobStatus.QUEUED),Map.entry("RESIZING",JobStatus.INPROGRESS),
        Map.entry("REVOKED",JobStatus.INPROGRESS),Map.entry("SIGNALING",JobStatus.INPROGRESS),Map.entry("SPECIAL_EXIT",JobStatus.INPROGRESS),
        Map.entry("STAGE_OUT",JobStatus.INPROGRESS),Map.entry("STOPPED",JobStatus.ABORTED),Map.entry("SUSPENDED",JobStatus.ABORTED),Map.entry("TIMEOUT",JobStatus.ABORTED));
    public static void initiateJob(Job j,JobRepository jobRepo)throws IOException{
        boolean local = j.getPlans() <= thresh;
        AlgorithmInputs inputs = new AlgorithmInputs(j, local);
        //Edit the slurm script based on params
        if (!local){
            String state = j.getState().getStateName().name().toLowerCase();
            String capState = state.substring(0,1).toUpperCase() + state.substring(1);
            String fn = "src/main/resources/colts_redistrict.slurm";
            String txt = "#!/usr/bin/env bash\n\n#SBATCH --job-name=colts_batch"+j.getJobId()+"\n#SBATCH --output="+j.getJobId()+".log\n#SBATCH --ntasks-per-node=40\n#SBATCH --nodes=2\n#SBATCH --time=96:00:00\n#SBATCH -p extended-40core\n#SBATCH --mail-type=BEGIN,END\n#SBATCH --mail-user=ethan.cheung@stonybrook.edu\n\nmodule load anaconda/3 \nmodule load mpi4py\n\nmpirun -np 300 --oversubscribe python /gpfs/projects/CSE416/Colts/algo.py "+j.getState().getNumDistricts()+" 1000000 "+ j.getPlans() + " "+ j.getCompactness()+" "+j.getPopulationDeviation()+" /gpfs/home/etcheung/CSE416/Colts/data/"+capState+"_Input.json /gpfs/home/etcheung/CSE416/Colts/Jobs/"+j.getJobId()+"/results.json";
            ServerDispatcher.editFile(fn, txt);
            fn = "src/main/resources/trigger.sh";
            txt = "sudo scp -i ./src/main/resources/cthan_key ./src/main/resources/colts_redistrict.slurm etcheung@login.seawulf.stonybrook.edu:/gpfs/projects/CSE416/Colts\nsudo ssh -i ./src/main/resources/cthan_key etcheung@login.seawulf.stonybrook.edu 'source /etc/profile.d/modules.sh;module load slurm; cd /gpfs/home/etcheung/CSE416/Colts/Jobs;mkdir "+j.getJobId()+";cd /gpfs/projects/CSE416/Colts;sbatch colts_redistrict.slurm'";
            ServerDispatcher.editFile(fn,txt);
            String result = ServerDispatcher.runScript(fn);
            String[] arr = result.split(" ");
            int slurmId = Integer.parseInt(arr[arr.length-1]);
            System.out.println(slurmId);
            j.setSlurmId(slurmId);
            jobRepo.save(j);
        }else{
            j.setStatus(JobStatus.INPROGRESS);
            jobRepo.save(j);
            initiateJobLocally(inputs);
        }
    }
    private static void initiateJobLocally(AlgorithmInputs inputs){
        try {
            ProcessBuilder builder = new ProcessBuilder("python", PathBuilder.getAlgorithmScript(), 
            inputs.targetDistricts, inputs.maxIterations, inputs.plans, inputs.compactness, inputs.populationDeviation, 
            inputs.inputFile, inputs.outputFile);
            builder.redirectErrorStream(true);
            builder.inheritIO().start();
            System.out.println("Started algorithm");
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static JobStatus seawulfStatus(int slurmId,int jobId,JobStatus js)throws IOException{
        if (!ServerDispatcher.canSSH()){
            return js;
        }
        String filename = "src/main/resources/status.sh";
        String text = "sudo ssh -i ./src/main/resources/cthan_key etcheung@login.seawulf.stonybrook.edu 'source /etc/profile.d/modules.sh;module load slurm;scontrol show job "+slurmId+"'";
        ServerDispatcher.editFile(filename,text);
        String result = ServerDispatcher.runScript(filename);
        if(result.equals("")){
            String fn = "src/main/resources/check_res.sh";
            String fnTxt = "sudo ssh -i ./src/main/resources/cthan_key etcheung@login.seawulf.stonybrook.edu 'cd ./CSE416/Colts/Jobs/"+jobId+";[ -f ./results.json ] && echo \"true\" || echo \"false\"'";
            ServerDispatcher.editFile(fn,fnTxt);
            String res = ServerDispatcher.runScript(fn);
            Boolean exists = Boolean.parseBoolean(res);
            if(exists){
                return JobStatus.COMPLETED;
            }
            return JobStatus.ABORTED;
        }else{
            String[] arr = result.split("JobState=");
            String strStatus = arr[arr.length-1].split(" ")[0];
            JobStatus status = null;
            if(statusMapping.containsKey(strStatus)){
                status = statusMapping.get(strStatus);
            }
            System.out.println(status);
            return status;
        }
    }
    public static void cancelJob(int slurmId)throws IOException{
        String filename = "src/main/resources/cancel.sh";
        String text = "sudo ssh -i ./src/main/resources/cthan_key etcheung@login.seawulf.stonybrook.edu 'source /etc/profile.d/modules.sh;module load slurm;scancel "+slurmId+"'";
        ServerDispatcher.editFile(filename,text);
        ServerDispatcher.runScript(filename);
    }
    public static void removeFiles(int jobId)throws IOException{
        String filename = "src/main/resources/removeFiles.sh";
        String text = "sudo ssh -i ./src/main/resources/cthan_key etcheung@login.seawulf.stonybrook.edu 'cd /gpfs/home/etcheung/CSE416/Colts/Jobs;rm -r "+jobId+"'";
        ServerDispatcher.editFile(filename,text);
        ServerDispatcher.runScript(filename);
        ServerDispatcher.deleteLocalFile("src/main/resources/jobs/"+jobId+"_districtings.json");
        ServerDispatcher.deleteLocalFile("src/main/resources/job_districts/"+jobId+"_extreme.json");
        ServerDispatcher.deleteLocalFile("src/main/resources/job_districts/"+jobId+"_average.json");
    }
    public static void retrieveResults(int jobId)throws IOException{
        String filename = "src/main/resources/retrieveResults.sh";
        String text = "sudo scp -i ./src/main/resources/cthan_key etcheung@login.seawulf.stonybrook.edu:/gpfs/home/etcheung/CSE416/Colts/Jobs/"+jobId+"/results.json ./src/main/resources/jobs/"+jobId+"_districtings.json";
        ServerDispatcher.editFile(filename,text);
        ServerDispatcher.runScript(filename);
    }
    private static String runScript(String filename) throws IOException{
            ProcessBuilder exec = new ProcessBuilder("bash",filename);
            Process process = exec.start();
            //Read the output created from running the script as an input stream
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ( (line = reader.readLine()) != null) {
                builder.append(line);
            }
            String result = builder.toString();
            return result;
    }
    private static void editFile(String filename,String text) throws IOException{
        File script = new File(filename);
        script.createNewFile();
        FileWriter myWriter = new FileWriter(filename);
        myWriter.write(text);
        myWriter.close();

    }
    public static Boolean canSSH()throws IOException{
        System.out.println("Testing SSH");
        String sshText = "sudo ssh -i ./src/main/resources/cthan_key etcheung@login.seawulf.stonybrook.edu 'echo \"true\"'";
        String fn = "src/main/resources/ssh_test.sh";
        ServerDispatcher.editFile(fn,sshText);
        String res = ServerDispatcher.runScript(fn);
        System.out.println(res);
        if(res.equals("")){
            return false;
        }else{
            return Boolean.parseBoolean(res);
        }
    }
    private static void deleteLocalFile(String fn){
        File f = new File(fn);
        if(f.exists()){
            f.delete();
        }
    }
}
