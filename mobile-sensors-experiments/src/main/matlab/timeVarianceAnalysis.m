function timeVarianceAnalysis( file )

data = load(file);

times = unique(data(:, 1));
xrange = unique(data(:, 2));
yrange = unique(data(:, 3));

for i = times'
    data_i = data(data(:, 1) == i, 4);
    surf(xrange, yrange, reshape(data_i, length(xrange), length(yrange)));
    pause;
end