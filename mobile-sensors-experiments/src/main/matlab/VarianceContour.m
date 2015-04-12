function VarianceContour( directory, createImages, pauseTime, varianceFile)
% call for individual sensor VarianceContour('run0000066/run00000/', '', 0.1, 'sensor0/variance0.txt');

figure;

if nargin < 1
   directory = '.';
end

if nargin < 3
    pauseTime = 0.5;
end

disp(pauseTime)

if nargin < 2
    createImages = 0;
end

if nargin < 4
    [variances xrange yrange timeRange] = LoadVariances(directory);    
else
    [variances xrange yrange timeRange] = LoadVariances(directory, varianceFile);    
end

vertices = LoadGraph(directory);

communicationRange = load([directory '/communicationRange.txt']);

locations = LoadLocations(directory);
maxVar = max(variances(:));

for i = 1:length(timeRange)
    clf;
    
    hold on;

    plotSensorGraph(locations, i, communicationRange);
    
    [C, h] = contour(xrange, yrange, variances(:, :, i));
    caxis([0 maxVar]);
%    set(h,'ShowText','on','TextStep',get(h,'LevelStep')*2);
    
    scatter(vertices(:, 2), vertices(:, 3), '.', 'SizeData', 30, 'MarkerEdgeColor', [0 0 0] )
    
    
    colorbar;
   
    
    %title(['t = ' t ' ' num2str(timeRange(i)) 's. / ' num2str(timeRange(end)) 's.'], 'FontSize', 12);

    axis equal
    xlim(xrange([1 end]));
    ylim(yrange([1 end]));

    xlabel('X', 'FontSize', 12);
    ylabel('Y', 'FontSize', 12);
    box on;
    
    set(gca,'FontName','Times','FontSize',12);
%    colorbar;
    set(gca, 'Position', [0.1 0.1 .8 .8]);
    
    hold off;
    
    if pauseTime < 100
        pause(pauseTime);
    else
        pause;
    end
    
    % r72 = 72 dpi. r300 = better resolution
    
    if createImages
        print('-djpeg', '-r150', ['fig' num2str(i, '%05d') '.jpg']);
    end
end