sudo scp -i ./src/main/resources/cthan_key ./src/main/resources/colts_redistrict.slurm etcheung@login.seawulf.stonybrook.edu:/gpfs/projects/CSE416/Colts
sudo ssh -i ./src/main/resources/cthan_key etcheung@login.seawulf.stonybrook.edu 'source /etc/profile.d/modules.sh;module load slurm; cd /gpfs/home/etcheung/CSE416/Colts/Jobs;mkdir 160;cd /gpfs/projects/CSE416/Colts;sbatch colts_redistrict.slurm'