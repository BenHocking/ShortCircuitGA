function data = getTargetFitness_2(first, last, trial, timeStep)
    data = [];
    timeIdx = floor(timeStep / 50) + 1;
    for i=first:last
        dirname=['randVol_',num2str(i)];
        script=[dirname,'/trace_full.nj'];
        mePct=readMePct(script);
        dd=load([dirname,'/fit2_',num2str(trial),'.dat']);
        d=dd(timeIdx);
        if (d > 0)
          d = d / mean(dd);
        end
        data=[data,d/(0.3*mePct)];
    end
end
