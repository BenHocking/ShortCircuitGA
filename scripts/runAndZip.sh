chmod a+x NeuroJet_linux
./NeuroJet_linux trace_full.nj
tar -cvzf trace_full.tgz fit*.dat fit*.ready
rm -f fit*.dat fit*.ready
