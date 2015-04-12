function WeightPlot( directory, i, paramIndex )
%WEIGHTPLOT Summary of this function goes here
%   Detailed explanation goes here

weightsFile = [directory '/' 'gp_weights' num2str(i)];

weights = load(weightsFile);

paramIndex = paramIndex + 2;
%uniqueWeights = unique(weights(:, paramIndex));

timeRange = unique(weights(:, 2));
%maxWeight = max(weights(:, end));

for i = 1 : length(timeRange)
    % put weights in matrix of the form (param_value, weight)
    w = weights(weights(:, 2) == timeRange(i), :);
    w = w(:, [paramIndex, end]);
    
    uniqueWeights = unique(w(:, 1));
    disp([timeRange(i) length(uniqueWeights)]);

    paramWeight = zeros(length(uniqueWeights), 2);
    
    % for each param, sum weights (marginalize over all other
    % parameters)
    for j = 1 : length(uniqueWeights)
        paramWeight(j, :) = [uniqueWeights(j) sum(w(w(:, 1) == uniqueWeights(j), 2))];
    end
    
    scatter(paramWeight(:, 1), paramWeight(:, 2));
    ylim([0, 1]);
    pause %(0.1);
end