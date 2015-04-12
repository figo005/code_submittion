results = [];

directories = dir('range*');

q = 1;
for i = 0:5:50
    averages = load(['range_' num2str(i) '/averages']);
    results(q, :) = [i mean(averages) std(averages) / sqrt(length(averages)) ];
    q = q + 1;
end

errorbar(results(:, 1),results(:, 2),results(:, 3));
