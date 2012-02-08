function plotPuffAvg(dir_num)

subdir=['trace_',num2str(dir_num),'/'];
script = [subdir, '/trace_full.nj'];
me = readVar(script, 'me');
buff=full(spconvert(load([subdir,'tstBuff.dat'])));
puff=buff(:,(me+1):(2*me));
puffAvg=mean(puff,2);
h = plot(puffAvg);
axis([0 750 0 0.15]);
ylabel('Activity');
xlabel('Time step (ms)');
