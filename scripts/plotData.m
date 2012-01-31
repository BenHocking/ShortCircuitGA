function plotData(dir_num)
  figure % new plot window
  colors={"r", "g", "b", "c-o", "m-o", "r-o"};
  subdir = ['trace_',num2str(dir_num),'/'];
  script = [subdir, 'trace_full.nj'];
  activityHz = readVar(script, 'ActivityHz');
  activity = activityHz * 1.0 / 1000;
  mePct = readVar(script, 'mePct');
  ni = 2048;
  extAct = ni * activityHz * mePct;
  me = round(ni * mePct / 10);
  desiredAct = extAct / me;
  handles = [];
  for i = 1:6
    n = i * 50;
    x = 1000 * load([subdir, 'fit2_', num2str(n), '.dat']);
    h = plot((1:length(x)) * 50, x, char(colors(i)));
    handles = [handles, h];
    hold on;
  end
  plot([1 750], 0.3 * [desiredAct desiredAct], 'k--');
  plot([1 750], [activityHz activityHz], 'k-.');
  legend("50", "100", "150", "200", "250", "300");
end
