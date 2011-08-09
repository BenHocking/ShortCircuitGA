function result = findDeciles(data)

    % Currently requires O(n lg n) time - could be sped up to only require O(n) time
    sortedData = sort(data);
    indices = round([0.1:0.1:0.9] * length(data));
    result = sortedData(indices);
    if (result(1) == result(2))
        nonzeroData = sortedData(sortedData>0);
        indices = round([(1/9):(1/9):(8/9)] * length(nonzeroData));
        result(2:end) = nonzeroData(indices);
    end

end