function plotLastTestActMvgAvg(dir_num)

subdir = ['trace_',num2str(dir_num),'/'];
me = load([subdir,'me.dat']);
activityHz = load([subdir,'ActivityHz.dat']);
buff = full(spconvert(load([subdir,'tstBuff.dat'])));
buffAvg = mean(buff, 2) * 1000;
buffMvgAvg = zeros(size(buffAvg, 1), 1);
mvgAvg = activityHz * 0.9;
decay = 0.01;
for i = 1:size(buffAvg,1)
  mvgAvg = (1 - decay) * mvgAvg + decay * buffAvg(i, 1);
  buffMvgAvg(i, 1) = mvgAvg;
end
h = plot(buffMvgAvg, 'r');
axis([0 750 0 10.5]);
title('Moving average of average activity');
ylabel('Activity');
xlabel('Time step (ms)');
