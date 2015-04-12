function rmse = PredictionQuality( directory)
% returns (time, RMSE) tuples

% time sensorID locationX locationY prediction actual
data = load([directory '/predictions.txt']);

times = unique(data(:, 1));

% times = unique(data(:, 1));
% xrange = unique(data(:, 2));
% yrange = unique(data(:, 3));
% 

rmse = zeros(length(times), 2);

for i = 1:length(times)
    time = times(i);
    predictions = data(data(:, 1) == time, 5);
    actual = data(data(:, 1) == time, 6);
    
    rmse(i, :) = [time sqrt(sum((predictions - actual).^2) / length(predictions))];
end