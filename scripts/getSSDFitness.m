function data = getSSDFitness(first, last, trialType)

    matching_ssd = 0.015;
    data=[];
    for i=first:last
        dirname=['randVol_',num2str(i)];
        script=[dirname,'/trace_full.nj'];
        desiredAct=readActivity(script);
        x=mean(load([dirname,'/fit_',trialType,'_ssd_act.dat']));
        if (x < matching_ssd)
            ssd_denom = 100 * (matching_ssd - x);
        else
            ssd_denom = x - matching_ssd;
        endif
        data=[data,(desiredAct*desiredAct)/ssd_denom];
    end
end
