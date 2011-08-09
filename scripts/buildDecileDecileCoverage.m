function result = buildDecileDecileCoverage(x, y)

    xDeciles = findDeciles(x);
    yDeciles = findDeciles(y);
    result = zeros(10, 10);
    for i = 1:length(x)
        xDecile = findDecilePlace(x(i), xDeciles);
        yDecile = findDecilePlace(y(i), yDeciles);
        result(xDecile, yDecile) += 1;
    end

end