function RMSEPlot( directory )

rmse = PredictionQuality(directory);
plot(rmse(:, 1), rmse(:, 2));