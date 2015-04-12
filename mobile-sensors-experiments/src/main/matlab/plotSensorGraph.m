function  plotSensorGraph( locations, timestep, communicationRange )

   


    currentLocations = locations(:, :, timestep);
    sensorCount = size(currentLocations, 1);

    for j = 1 : sensorCount
        scatter(currentLocations(j, 1), currentLocations(j, 2), '.', 'SizeData', 1000); %, 'MarkerEdgeColor', [1 0 0]);
    end
    
    for i = 1 : sensorCount
       for j = 1 : i - 1 
          sensor1Coords = currentLocations(i, :);
          sensor2Coords = currentLocations(j, :);
          
          
          distance = sqrt(sum((sensor1Coords - sensor2Coords).^2));
          
          if(distance <= communicationRange)
              x = [sensor1Coords(1) sensor2Coords(1)];
              y = [sensor1Coords(2) sensor2Coords(2)];
              line(x, y);
              
              middleX = sum(x) / 2;
              middleY = sum(y) / 2 ;
          %    text(middleX, middleY, num2str(distance));
          end
       end
    end

end