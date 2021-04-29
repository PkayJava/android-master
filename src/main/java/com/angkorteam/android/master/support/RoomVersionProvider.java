package com.angkorteam.android.master.support;

import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * androidx.room:room-runtime
 */
@Component
public class RoomVersionProvider extends ValueProviderSupport {

    public static final String V_2_3_0 = "2.3.0";

    public static final String V_2_4_0_ALPHA01 = "2.4.0-alpha01";

    public static final String SELECTED = V_2_4_0_ALPHA01;

    @Override
    public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
        List<CompletionProposal> proposal = new ArrayList<>();
        proposal.add(new CompletionProposal(V_2_3_0));
        proposal.add(new CompletionProposal(V_2_4_0_ALPHA01));
        return proposal;
    }

}