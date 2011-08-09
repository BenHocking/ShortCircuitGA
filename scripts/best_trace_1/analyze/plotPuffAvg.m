function plotPuffAvg(numTrials)

me=load('me.dat');
for i=1:numTrials
  trialStr=num2str(1000+i)(2:4);
  buff=full(spconvert(load(['tstBuff_',trialStr,'.dat'])));
  puff=buff(:,(me+1):(2*me));
  puffAvg=mean(puff,2);
  plot(puffAvg);
  axis([0 750 0 0.15]);
  title(['Testing trial #',num2str(i)]);
  ylabel('Activity');
  xlabel('Time step (ms)');
  pause
  saveas(['puffAvg_',trialStr,'.png']);
end

