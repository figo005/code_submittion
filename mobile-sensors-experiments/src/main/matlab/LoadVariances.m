function [variances xRange yRange timeRange] = LoadVariances( directory, varianceFile )
%LOADVARIANCES Summary of this function goes here
% output should be a three dim (n, 3, t) 
% matrix with x, y, variance, and time as last index
% nonexistent x, y pairs are filled with NaN values

% this is a file with (time, x, y, variance) tuples
if nargin < 2
    varianceFile = [directory '/' 'spatialFieldValues.txt'];    
else
    varianceFile = [directory '/' varianceFile];    
end


if not(exist(varianceFile, 'file'))
    zipped = [varianceFile '.gz'];
    if(exist(zipped, 'file'))
        gunzip(zipped);
    else
       error(['No file found ' zipped]); 
    end
end

variance = load(varianceFile);

xRange = unique(variance(:, 2));
yRange = unique(variance(:, 3));
timeRange = unique(variance(:, 1));

minX = min(xRange);
minY = min(yRange);
maxX = max(xRange);
maxY = max(yRange);

variances = NaN(length(yRange), length(xRange), length(timeRange));

for i = 1:length(variance)
    var = variance(i, :);
    variances(yRange == var(3), xRange == var(2), timeRange == var(1)) = var(4);
end