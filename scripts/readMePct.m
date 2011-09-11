function result = readMePct(script)

    [s,w]=system(['grep "mePct [0-9].[0-9][^)]" ', script]);
    commentPos = findstr(w,'#');
    result = eval(w(9:(commentPos-1)));

end
