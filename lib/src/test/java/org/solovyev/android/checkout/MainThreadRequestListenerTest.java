/*
 * Copyright 2014 serso aka se.solovyev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Contact details
 *
 * Email: se.solovyev@gmail.com
 * Site:  http://se.solovyev.org
 */

package org.solovyev.android.checkout;

import org.junit.Test;

import javax.annotation.Nonnull;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MainThreadRequestListenerTest {

    @Test
    public void testShouldCallOnSuccess() throws Exception {
        final RequestListener l = mock(RequestListener.class);
        final MainThreadRequestListener mtl = new MainThreadRequestListener(Tests.sameThreadExecutor(), l);

        final Object o = new Object();
        mtl.onSuccess(o);

        verify(l).onSuccess(eq(o));
    }

    @Test
    public void testShouldCallOnError() throws Exception {
        final RequestListener l = mock(RequestListener.class);
        final MainThreadRequestListener mtl = new MainThreadRequestListener(Tests.sameThreadExecutor(), l);

        final Exception e = new Exception();
        mtl.onError(3, e);

        verify(l).onError(eq(3), eq(e));
    }

    @Test
    public void testShouldCancelSuccessRunnable() throws Exception {
        final RequestListener l = mock(RequestListener.class);
        final CancellableExecutor executor = new TestExecutor();
        final MainThreadRequestListener mtl = new MainThreadRequestListener(executor, l);

        mtl.onSuccess(new Object());

        mtl.cancel();
    }

    @Test
    public void testShouldCancelErrorRunnable() throws Exception {
        final RequestListener l = mock(RequestListener.class);
        final CancellableExecutor executor = new TestExecutor();
        final MainThreadRequestListener mtl = new MainThreadRequestListener(executor, l);

        mtl.onError(3, new Exception());

        mtl.cancel();
    }

    private static class TestExecutor implements CancellableExecutor {

        private Runnable mExecuting;

        @Override
        public void execute(@Nonnull Runnable runnable) {
            assertNull(mExecuting);
            mExecuting = runnable;
        }

        @Override
        public void cancel(@Nonnull Runnable runnable) {
            assertNotNull(mExecuting);
            assertSame(mExecuting, runnable);
        }
    }
}