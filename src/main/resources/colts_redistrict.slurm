#!/usr/bin/env bash

#SBATCH --job-name=colts_batch160
#SBATCH --output=160.log
#SBATCH --ntasks-per-node=40
#SBATCH --nodes=2
#SBATCH --time=96:00:00
#SBATCH -p extended-40core
#SBATCH --mail-type=BEGIN,END
#SBATCH --mail-user=ethan.cheung@stonybrook.edu

module load anaconda/3 
module load mpi4py

mpirun -np 300 --oversubscribe python /gpfs/projects/CSE416/Colts/algo.py 7 1000000 100 0.75 0.1 /gpfs/home/etcheung/CSE416/Colts/data/Alabama_Input.json /gpfs/home/etcheung/CSE416/Colts/Jobs/160/results.json