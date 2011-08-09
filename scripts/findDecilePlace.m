function result = findDecilePlace(value, deciles)

    if value >= deciles(end)
        result = 10;
    else
        % Find first decile greater than this value
        result = find(deciles >=a value)(1);
    end

end