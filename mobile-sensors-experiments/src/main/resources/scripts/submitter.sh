mkdir logs

scripts_dir=scripts

for script in $(ls $scripts_dir/)
do
	echo $script
	qsub $scripts_dir/$script
done
