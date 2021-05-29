package com.angkorteam.android.master.support;

import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * https://mvnrepository.com/artifact/androidx.hilt/hilt-lifecycle-viewmodel
 * androidx.hilt:hilt-lifecycle-viewmodel
 */
@Component
public class HiltLifecycleViewModelVersionProvider extends ValueProviderSupport {

    public static final String V_1_0_0_ALPHA03 = "1.0.0-alpha03";
    public static final String SELECTED = V_1_0_0_ALPHA03;

    @Override
    public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
        List<CompletionProposal> proposal = new ArrayList<>();
        proposal.add(new CompletionProposal(V_1_0_0_ALPHA03));
        return proposal;
    }

}