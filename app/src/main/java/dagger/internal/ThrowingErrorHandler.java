package dagger.internal;

import dagger.internal.Linker;
import java.util.List;

/* loaded from: classes.dex */
public final class ThrowingErrorHandler implements Linker.ErrorHandler {
    @Override // dagger.internal.Linker.ErrorHandler
    public void handleErrors(List<String> errors) {
        if (errors.isEmpty()) {
            return;
        }
        StringBuilder message = new StringBuilder();
        message.append("Errors creating object graph:");
        for (String error : errors) {
            message.append("\n  ").append(error);
        }
        throw new IllegalStateException(message.toString());
    }
}
