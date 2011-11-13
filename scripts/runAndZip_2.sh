chmod a+x NeuroJet_linux
./NeuroJet_linux trace_full_1.nj
tar -cvzf trace_full_1.tgz fit*.dat fit*.ready
rm -f fit*.dat fit*.ready
./NeuroJet_linux trace_full_2.nj
tar -cvzf trace_full_2.tgz fit*.dat fit*.ready
rm -f fit*.dat fit*.ready
