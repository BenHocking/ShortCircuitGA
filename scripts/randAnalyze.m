function [xvals, yvals] = randAnalyze(first, last, trial, timeStep)

    timeIdx = floor(timeStep / 50) + 1;
    xvals=[];
    yvals=[];
    for i=first:last
        disp(i);
        dirname=['randVol_',num2str(i)];
        x=mean(load([dirname,'/fit_tst_mean_act.dat']));
        y1=load([dirname,'/fit2_',numstr(trial),'.dat']);
        y=y1(timeIdx);
        xvals=[xvals,x];
        yvals=[yvals,y];
    end
end