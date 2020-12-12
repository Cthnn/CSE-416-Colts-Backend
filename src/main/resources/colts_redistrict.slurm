#!/usr/bin/env bash

#SBATCH --job-name=colts_batch128
#SBATCH --output=128.log
#SBATCH --ntasks-per-node=40
#SBATCH --nodes=2
#SBATCH --time=96:00:00
#SBATCH -p extended-40core
#SBATCH --mail-type=BEGIN,END
#SBATCH --mail-user=ethan.cheung@stonybrook.edu

module load anaconda/3 
module load mpi4py

mpirun -np 1200 --oversubscribe python /gpfs/projects/CSE416/Colts/algo.py 11 1000000 1000 0.5 0.25 /gpfs/home/etcheung/CSE416/Colts/data/Virginia_Input.json /gpfs/home/etcheung/CSE416/Colts/Jobs/128/results.json