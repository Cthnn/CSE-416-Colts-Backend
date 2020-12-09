package com.example.demo;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import com.example.demo.EnumClasses.JobStatus;
import com.example.demo.PersistenceClasses.Job;
public class ServerDispatcher {
    private static int thresh = 0;
    private static Map<String,JobStatus> statusMapping= Map.ofEntries(Map.entry("BOOT_FAIL",JobStatus.ABORTED),
        Map.entry("CANCELLED",JobStatus.CANCELLED),Map.entry("COMPLETED",JobStatus.COMPLETED),Map.entry("CONFIGURING",JobStatus.QUEUED),
        Map.entry("COMPLETING",JobStatus.INPROGRESS),Map.entry("DEADLINE",JobStatus.ABORTED),Map.entry("FAILED",JobStatus.ABORTED),
        Map.entry("NODE_FAIL",JobStatus.ABORTED),Map.entry("OUT_OF_MEMORY",JobStatus.ABORTED),Map.entry("PENDING",JobStatus.QUEUED),
        Map.entry("PREEMPTED",JobStatus.ABORTED),Map.entry("RUNNING",JobStatus.INPROGRESS),Map.entry("RESV_DEL_HOLD",JobStatus.INPROGRESS),
        Map.entry("REQUEUE_FED",JobStatus.INPROGRESS),Map.entry("REQUEUED",JobStatus.QUEUED),Map.entry("RESIZING",JobStatus.INPROGRESS),
        Map.entry("REVOKED",JobStatus.INPROGRESS),Map.entry("SIGNALING",JobStatus.INPROGRESS),Map.entry("SPECIAL_EXIT",JobStatus.INPROGRESS),
        Map.entry("STAGE_OUT",JobStatus.INPROGRESS),Map.entry("STOPPED",JobStatus.ABORTED),Map.entry("SUSPENDED",JobStatus.ABORTED),Map.entry("TIMEOUT",JobStatus.ABORTED));
    public static int initiateJob(Job j)throws IOException{
        //Edit the slurm script based on params
        int tasks = 30;
        int nodes = 1;
        String time = "00:05:00";
        if (j.getPlans() > thresh){
            String fn = "src/main/resources/colts_redistrict.slurm";
            String txt = "#!/usr/bin/env bash\n\n#SBATCH --job-name=colts_redistrict\n#SBATCH --output=colts_test.log\n#SBATCH --ntasks-per-node="+tasks+"\n#SBATCH --nodes="+nodes+"\n#SBATCH --time="+time+"\n#SBATCH -p short-40core\n#SBATCH --mail-type=BEGIN,END\n#SBATCH --mail-user=ethan.cheung@stonybrook.edu\n\nmodule load python \npython /gpfs/projects/CSE416/Colts/algo.py";
            ServerDispatcher.editFile(fn, txt);
            fn = "src/main/resources/trigger.sh";
            txt = "sudo scp -i ./src/main/resources/cthan_key ./src/main/resources/colts_redistrict.slurm etcheung@login.seawulf.stonybrook.edu:/gpfs/projects/CSE416/Colts\nsudo ssh -i ./src/main/resources/cthan_key etcheung@login.seawulf.stonybrook.edu 'source /etc/profile.d/modules.sh;module load slurm; cd /gpfs/home/etcheung/CSE416/Colts/Jobs;mkdir "+j.getJobId()+";cd /gpfs/projects/CSE416/Colts;sbatch colts_redistrict.slurm'";
            ServerDispatcher.editFile(fn,txt);
            String result = ServerDispatcher.runScript(fn);
            String[] arr = result.split(" ");
            int slurmId = Integer.parseInt(arr[arr.length-1]);
            System.out.println(slurmId);
            return slurmId;
        }else{
            //Run Locally
            return -1;
        }
    }
    public static JobStatus seawulfStatus(int slurmId)throws IOException{
        String filename = "src/main/resources/status.sh";
        String text = "sudo ssh -i ./src/main/resources/cthan_key etcheung@login.seawulf.stonybrook.edu 'source /etc/profile.d/modules.sh;module load slurm;scontrol show job "+slurmId+"'";
        ServerDispatcher.editFile(filename,text);
        String result = ServerDispatcher.runScript(filename);
        System.out.println("Empty");
        if(result.equals(" ")){
            System.out.println("Empty");
            //Check if folder is empty
            return JobStatus.COMPLETED;
        }else{
            String[] arr = result.split("JobState=");
            String strStatus = arr[arr.length-1].split(" ")[0];
            JobStatus status = null;
            if(statusMapping.containsKey(strStatus)){
                status = statusMapping.get(strStatus);
            }
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
}
