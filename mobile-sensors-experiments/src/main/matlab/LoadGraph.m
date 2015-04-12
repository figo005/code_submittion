function vertices = LoadGraph( directory )
%DRAWGRAPH Summary of this function goes here
%   Detailed explanation goes here


fileName = [directory '/graph.txt'];
fid = fopen(fileName);

dataline = fgetl(fid);
vertexCount = str2num(dataline([10:end]));

vertices = zeros(vertexCount, 3);

for i = 1 : vertexCount
    dataline = fgetl(fid);
    vertices(i, :) = str2num(dataline);
end


%scatter(vertices(:, 2), vertices(:, 3), '.', 'MarkerEdgeColor', [0.6 0.6 0.6] )

fclose(fid);