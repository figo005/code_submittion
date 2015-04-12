function locations = LoadLocations( directory )
% returns a 3d matrix with dim1 = sensorID dim2 = (x,y) dim3=timestep

sensorDirectoryPrefix = [directory '/sensor'];

disp(sensorDirectoryPrefix)
exist([sensorDirectoryPrefix 0])

i = 0;


while exist([sensorDirectoryPrefix int2str(i)])
    raw(:, :, i+1) = load([sensorDirectoryPrefix int2str(i) '/sensor_locations']);
    
    i = i + 1;
end

sensorCount = size(raw, 3);
timeSteps = size(raw, 1);

locations = zeros(sensorCount, 2, timeSteps);


for i = 1:timeSteps
    for j = 1:sensorCount
        locations(j, :, i) = raw(i, [3 4], j);
    end
end



