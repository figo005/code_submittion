function rms = RMSPlot( directory )

figure;

file = [directory '/' 'average-RMS.txt'];

rms = load(file);


plot(rms(:, 2));
ylim([0 max(rms(:, 2))]);