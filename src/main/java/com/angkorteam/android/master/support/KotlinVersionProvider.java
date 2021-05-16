package com.angkorteam.android.master.support;

import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-gradle-plugin
 * org.jetbrains.kotlin:kotlin-gradle-plugin
 */
@Component
public class KotlinVersionProvider extends ValueProviderSupport {

    public static final String V_1_4_30 = "1.4.30";

    public static final String V_1_4_31 = "1.4.31";

    public static final String V_1_4_32 = "1.4.32";

    public static final String V_1_5_0 = "1.5.0";

    public static final String SELECTED = V_1_4_32;

    @Override
    public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
        List<CompletionProposal> proposal = new ArrayList<>();
        proposal.add(new CompletionProposal(V_1_4_30));
        proposal.add(new CompletionProposal(V_1_4_31));
        proposal.add(new CompletionProposal(V_1_4_32));
        return proposal;
    }

}