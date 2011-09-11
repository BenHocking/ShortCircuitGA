function result = readActivity(script)

    [s,w]=system(['grep "ActivityHz [0-9].[0-9]" ', script]);
    result = eval(w(14:end));

end
