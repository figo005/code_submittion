mkdir logs

for simulation in $(ls simulations/)
do
	echo $simulation
	java -jar experiments.jar -sc 5 -rc 1 -sim simulations/$simulation -sense sensor_entropy_learning.xml -output runs/run >logs/$simulation.log 2>logs/$simulation.err
done
