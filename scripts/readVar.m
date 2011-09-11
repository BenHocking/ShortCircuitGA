function result = readVar(script, var)

    [s,w]=system(['grep -E "  ', var, ' [0-9]\.?[0-9]*" ', script, ' | psed -e "s/  ', var, ' \([0-9]*.\{0,1\}[0-9]*\).*/\1/"']);
    result = str2num(w);

end
