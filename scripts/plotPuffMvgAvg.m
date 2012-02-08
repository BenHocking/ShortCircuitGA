function plotPuffMvgAvg(dir_num)

subdir = ['trace_',num2str(dir_num),'/'];
script = [subdir, 'trace_full.nj'];
activityHz = readVar(script, 'ActivityHz');
mePct = readVar(script, 'mePct');
ni = 2048;
extAct = ni * activityHz * mePct;
me = round(ni * mePct / 10);
desiredAct = extAct / me;
buff = full(spconvert(load([subdir,'tstBuff.dat'])));
puff = buff(:, (me+1):(2*me));
puffAvg = mean(puff, 2) * 1000;
puffMvgAvg = zeros(size(puffAvg, 1), 1);
mvgAvg = desiredAct * 0.18;
decay = 0.01;
for i = 1:size(puffAvg,1)
  mvgAvg = (1 - decay) * mvgAvg + decay * puffAvg(i, 1);
  puffMvgAvg(i, 1) = mvgAvg;
end
h = plot(puffMvgAvg);
axis([0 750 0 10.5]);
title('Moving average of puff neuron average activity');
ylabel('Activity');
xlabel('Time step (ms)');
