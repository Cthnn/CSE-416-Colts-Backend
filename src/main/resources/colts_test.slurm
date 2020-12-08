#!/usr/bin/env bash

#SBATCH --job-name=colts_test
#SBATCH --output=colts_test.log
#SBATCH --ntasks-per-node=30
#SBATCH --nodes=1
#SBATCH --time=00:05:00
#SBATCH -p short-40core
#SBATCH --mail-type=BEGIN,END
#SBATCH --mail-user=ethan.cheung@stonybrook.edu

module load python

python /gpfs/projects/CSE416/Colts/test.py

