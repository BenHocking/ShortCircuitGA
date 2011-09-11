function data = getPredictorFitness(first, last, trialType)

    matching_ssd = 0.015;
    data=[];
    for i=first:last
        dirname=['randVol_',num2str(i)];
        script=[dirname,'/trace_full.nj'];
        desiredAct=readActivity(script);
        meanAct=load([dirname,'/fit_',trialType,'_mean_act.dat']);
        actualAct=mean(meanAct);
        deltaAct=meanAct - desiredAct;
        x=mean(load([dirname,'/fit_',trialType,'_ssd_act.dat']));
        if (x < matching_ssd)
            ssd_denom = 100 * (matching_ssd - x);
        else
            ssd_denom = x - matching_ssd;
        endif
	ssd_fitness = actualAct*actualAct / ssd_denom;
        mean_fitness = 100 / sum(meanAct .* meanAct);
        data=[data, mean_fitness + ssd_fitness];
    end
end
