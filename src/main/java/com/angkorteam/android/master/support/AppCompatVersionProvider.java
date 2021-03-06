package com.angkorteam.android.master.support;

import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * https://mvnrepository.com/artifact/androidx.appcompat/appcompat
 * androidx.appcompat:appcompat
 */
@Component
public class AppCompatVersionProvider extends ValueProviderSupport {

    public static final String V_1_2_0 = "1.2.0";

    public static final String V_1_3_0_RC01 = "1.3.0-rc01";

    public static final String V_1_4_0_ALPHA01 = "1.4.0-alpha01";

    public static final String SELECTED = V_1_4_0_ALPHA01;

    @Override
    public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
        List<CompletionProposal> proposal = new ArrayList<>();
        proposal.add(new CompletionProposal(V_1_2_0));
        proposal.add(new CompletionProposal(V_1_3_0_RC01));
        proposal.add(new CompletionProposal(V_1_4_0_ALPHA01));
        return proposal;
    }

}