#!/usr/bin/env bash

#SBATCH --job-name=colts_batch147
#SBATCH --output=147.log
#SBATCH --ntasks-per-node=40
#SBATCH --nodes=2
#SBATCH --time=96:00:00
#SBATCH -p extended-40core
#SBATCH --mail-type=BEGIN,END
#SBATCH --mail-user=ethan.cheung@stonybrook.edu

module load anaconda/3 
module load mpi4py

mpirun -np 101 --oversubscribe python /gpfs/projects/CSE416/Colts/algo.py 11 1000000 100 0.75 0.03 /gpfs/home/etcheung/CSE416/Colts/data/Virginia_Input.json /gpfs/home/etcheung/CSE416/Colts/Jobs/147/results.json