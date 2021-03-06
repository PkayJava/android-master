package com.angkorteam.android.master.support;

import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * https://mvnrepository.com/artifact/androidx.activity/activity-compose
 * androidx.activity:activity-compose
 */
@Component
public class ActivityComposeVersionProvider extends ValueProviderSupport {

    public static final String V_1_3_0_ALPHA03 = "1.3.0-alpha03";

    public static final String V_1_3_0_ALPHA04 = "1.3.0-alpha04";

    public static final String V_1_3_0_ALPHA05 = "1.3.0-alpha05";

    public static final String V_1_3_0_ALPHA06 = "1.3.0-alpha06";

    public static final String V_1_3_0_ALPHA07 = "1.3.0-alpha07";

    public static final String V_1_3_0_ALPHA08 = "1.3.0-alpha08";

    public static final String SELECTED = V_1_3_0_ALPHA08;

    @Override
    public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
        List<CompletionProposal> proposal = new ArrayList<>();
        proposal.add(new CompletionProposal(V_1_3_0_ALPHA03));
        proposal.add(new CompletionProposal(V_1_3_0_ALPHA04));
        proposal.add(new CompletionProposal(V_1_3_0_ALPHA05));
        proposal.add(new CompletionProposal(V_1_3_0_ALPHA06));
        proposal.add(new CompletionProposal(V_1_3_0_ALPHA07));
        proposal.add(new CompletionProposal(V_1_3_0_ALPHA08));
        return proposal;
    }

}