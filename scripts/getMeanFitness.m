function data = getMeanFitness(first, last, trialType)

    data=[];
    for i=first:last
        dirname=['randVol_',num2str(i)];
        script=[dirname,'/trace_full.nj'];
        desiredAct=readActivity(script);
        x=load([dirname,'/fit_',trialType,'_mean_act.dat']) - desiredAct;
        ssx=mean(x .* x);
        data=[data,1000 / ssx];
    end
end
