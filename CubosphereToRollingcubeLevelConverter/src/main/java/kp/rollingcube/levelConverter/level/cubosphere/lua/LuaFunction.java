package kp.rollingcube.levelConverter.level.cubosphere.lua;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.classdump.luna.impl.NonsuspendableFunctionException;
import org.classdump.luna.runtime.AbstractFunction0;
import org.classdump.luna.runtime.AbstractFunction1;
import org.classdump.luna.runtime.AbstractFunction2;
import org.classdump.luna.runtime.AbstractFunction3;
import org.classdump.luna.runtime.AbstractFunction4;
import org.classdump.luna.runtime.AbstractFunctionAnyArg;
import org.classdump.luna.runtime.ExecutionContext;
import org.classdump.luna.runtime.ResolvedControlThrowable;

/**
 *
 * @author Marc
 */
@UtilityClass
public class LuaFunction
{
    public @NonNull AbstractFunction0 of(@NonNull Closures.ZeroArgsVoid fn) { return new Templates.ZeroArgsVoidFunction(fn); }
    public <T> @NonNull AbstractFunction1 of(@NonNull Closures.OneArgsVoid<T> fn) { return new Templates.OneArgsVoidFunction(fn); }
    public <T1, T2> @NonNull AbstractFunction2 of(@NonNull Closures.TwoArgsVoid<T1, T2> fn) { return new Templates.TwoArgsVoidFunction(fn); }
    public <T1, T2, T3> @NonNull AbstractFunction3 of(@NonNull Closures.ThreeArgsVoid<T1, T2, T3> fn) { return new Templates.ThreeArgsVoidFunction(fn); }
    public <T1, T2, T3, T4> @NonNull AbstractFunction4 of(@NonNull Closures.FourArgsVoid<T1, T2, T3, T4> fn) { return new Templates.FourArgsVoidFunction(fn); }
    public @NonNull AbstractFunctionAnyArg of(@NonNull Closures.VarArgsVoid fn) { return new Templates.VarArgsVoidFunction(fn); }
    
    public @NonNull AbstractFunction0 of(@NonNull Closures.ZeroArgs fn) { return new Templates.ZeroArgsFunction(fn); }
    public <T> @NonNull AbstractFunction1 of(@NonNull Closures.OneArgs<T> fn) { return new Templates.OneArgsFunction(fn); }
    public <T1, T2> @NonNull AbstractFunction2 of(@NonNull Closures.TwoArgs<T1, T2> fn) { return new Templates.TwoArgsFunction(fn); }
    public <T1, T2, T3> @NonNull AbstractFunction3 of(@NonNull Closures.ThreeArgs<T1, T2, T3> fn) { return new Templates.ThreeArgsFunction(fn); }
    public <T1, T2, T3, T4> @NonNull AbstractFunction4 of(@NonNull Closures.FourArgs<T1, T2, T3, T4> fn) { return new Templates.FourArgsFunction(fn); }
    public @NonNull AbstractFunctionAnyArg of(@NonNull Closures.VarArgs fn) { return new Templates.VarArgsFunction(fn); }
    
    public AbstractFunction0 withContext(final @NonNull Closures.OneArgs<ExecutionContext> fn)
    {
        return new AbstractFunction0()
        {
            @Override
            public void invoke(ExecutionContext context) throws ResolvedControlThrowable
            {
                var result = fn.invoke(context);
                if(result != null)
                    context.getReturnBuffer().setTo(result);
                else
                    context.getReturnBuffer().setTo();
            }

            @Override
            public void resume(ExecutionContext context, Object suspendedState) throws ResolvedControlThrowable
            {
                fn.resume(context, suspendedState);
            }

        };
    }
    
    
    @UtilityClass
    public class Templates
    {
        private class ZeroArgsVoidFunction extends AbstractFunction0
        {
            private final @NonNull Closures.ZeroArgsVoid fn;
            
            private ZeroArgsVoidFunction(@NonNull Closures.ZeroArgsVoid fn) { this.fn = fn; }

            @Override
            public void invoke(ExecutionContext context) throws ResolvedControlThrowable
            {
                fn.invoke();
                context.getReturnBuffer().setTo();
            }

            @Override
            public void resume(ExecutionContext context, Object suspendedState) throws ResolvedControlThrowable
            {
                fn.resume(context, suspendedState);
            }
        }
        
        private class OneArgsVoidFunction<T> extends AbstractFunction1<T>
        {
            private final @NonNull Closures.OneArgsVoid<T> fn;
            
            private OneArgsVoidFunction(@NonNull Closures.OneArgsVoid<T> fn) { this.fn = fn; }

            @Override
            public void invoke(ExecutionContext context, T arg) throws ResolvedControlThrowable
            {
                fn.invoke(arg);
                context.getReturnBuffer().setTo();
            }

            @Override
            public void resume(ExecutionContext context, Object suspendedState) throws ResolvedControlThrowable
            {
                fn.resume(context, suspendedState);
            }
        }
        
        private class TwoArgsVoidFunction<T1, T2> extends AbstractFunction2<T1, T2>
        {
            private final @NonNull Closures.TwoArgsVoid<T1, T2> fn;
            
            private TwoArgsVoidFunction(@NonNull Closures.TwoArgsVoid<T1, T2> fn) { this.fn = fn; }

            @Override
            public void invoke(ExecutionContext context, T1 arg1, T2 arg2) throws ResolvedControlThrowable
            {
                fn.invoke(arg1, arg2);
                context.getReturnBuffer().setTo();
            }

            @Override
            public void resume(ExecutionContext context, Object suspendedState) throws ResolvedControlThrowable
            {
                fn.resume(context, suspendedState);
            }
        }
        
        private class ThreeArgsVoidFunction<T1, T2, T3> extends AbstractFunction3<T1, T2, T3>
        {
            private final @NonNull Closures.ThreeArgsVoid<T1, T2, T3> fn;
            
            private ThreeArgsVoidFunction(@NonNull Closures.ThreeArgsVoid<T1, T2, T3> fn) { this.fn = fn; }

            @Override
            public void invoke(ExecutionContext context, T1 arg1, T2 arg2, T3 arg3) throws ResolvedControlThrowable
            {
                fn.invoke(arg1, arg2, arg3);
                context.getReturnBuffer().setTo();
            }

            @Override
            public void resume(ExecutionContext context, Object suspendedState) throws ResolvedControlThrowable
            {
                fn.resume(context, suspendedState);
            }
        }
        
        private class FourArgsVoidFunction<T1, T2, T3, T4> extends AbstractFunction4<T1, T2, T3, T4>
        {
            private final @NonNull Closures.FourArgsVoid<T1, T2, T3, T4> fn;
            
            private FourArgsVoidFunction(@NonNull Closures.FourArgsVoid<T1, T2, T3, T4> fn) { this.fn = fn; }

            @Override
            public void invoke(ExecutionContext context, T1 arg1, T2 arg2, T3 arg3, T4 arg4) throws ResolvedControlThrowable
            {
                fn.invoke(arg1, arg2, arg3, arg4);
                context.getReturnBuffer().setTo();
            }

            @Override
            public void resume(ExecutionContext context, Object suspendedState) throws ResolvedControlThrowable
            {
                fn.resume(context, suspendedState);
            }
        }
        
        private class VarArgsVoidFunction extends AbstractFunctionAnyArg
        {
            private final @NonNull Closures.VarArgsVoid fn;
            
            private VarArgsVoidFunction(@NonNull Closures.VarArgsVoid fn) { this.fn = fn; }

            @Override
            public void invoke(ExecutionContext context, Object[] args) throws ResolvedControlThrowable
            {
                fn.invoke(args);
                context.getReturnBuffer().setTo();
            }

            @Override
            public void resume(ExecutionContext context, Object suspendedState) throws ResolvedControlThrowable
            {
                fn.resume(context, suspendedState);
            }
        }
        
        private class ZeroArgsFunction extends AbstractFunction0
        {
            private final @NonNull Closures.ZeroArgs fn;
            
            private ZeroArgsFunction(@NonNull Closures.ZeroArgs fn) { this.fn = fn; }

            @Override
            public void invoke(ExecutionContext context) throws ResolvedControlThrowable
            {
                context.getReturnBuffer().setTo(fn.invoke());
            }

            @Override
            public void resume(ExecutionContext context, Object suspendedState) throws ResolvedControlThrowable
            {
                fn.resume(context, suspendedState);
            }
        }
        
        private class OneArgsFunction<T> extends AbstractFunction1<T>
        {
            private final @NonNull Closures.OneArgs<T> fn;
            
            private OneArgsFunction(@NonNull Closures.OneArgs<T> fn) { this.fn = fn; }

            @Override
            public void invoke(ExecutionContext context, T arg) throws ResolvedControlThrowable
            {
                context.getReturnBuffer().setTo(fn.invoke(arg));
            }

            @Override
            public void resume(ExecutionContext context, Object suspendedState) throws ResolvedControlThrowable
            {
                fn.resume(context, suspendedState);
            }
        }
        
        private class TwoArgsFunction<T1, T2> extends AbstractFunction2<T1, T2>
        {
            private final @NonNull Closures.TwoArgs<T1, T2> fn;
            
            private TwoArgsFunction(@NonNull Closures.TwoArgs<T1, T2> fn) { this.fn = fn; }

            @Override
            public void invoke(ExecutionContext context, T1 arg1, T2 arg2) throws ResolvedControlThrowable
            {
                context.getReturnBuffer().setTo(fn.invoke(arg1, arg2));
            }

            @Override
            public void resume(ExecutionContext context, Object suspendedState) throws ResolvedControlThrowable
            {
                fn.resume(context, suspendedState);
            }
        }
        
        private class ThreeArgsFunction<T1, T2, T3> extends AbstractFunction3<T1, T2, T3>
        {
            private final @NonNull Closures.ThreeArgs<T1, T2, T3> fn;
            
            private ThreeArgsFunction(@NonNull Closures.ThreeArgs<T1, T2, T3> fn) { this.fn = fn; }

            @Override
            public void invoke(ExecutionContext context, T1 arg1, T2 arg2, T3 arg3) throws ResolvedControlThrowable
            {
                context.getReturnBuffer().setTo(fn.invoke(arg1, arg2, arg3));
            }

            @Override
            public void resume(ExecutionContext context, Object suspendedState) throws ResolvedControlThrowable
            {
                fn.resume(context, suspendedState);
            }
        }
        
        private class FourArgsFunction<T1, T2, T3, T4> extends AbstractFunction4<T1, T2, T3, T4>
        {
            private final @NonNull Closures.FourArgs<T1, T2, T3, T4> fn;
            
            private FourArgsFunction(@NonNull Closures.FourArgs<T1, T2, T3, T4> fn) { this.fn = fn; }

            @Override
            public void invoke(ExecutionContext context, T1 arg1, T2 arg2, T3 arg3, T4 arg4) throws ResolvedControlThrowable
            {
                context.getReturnBuffer().setTo(fn.invoke(arg1, arg2, arg3, arg4));
            }

            @Override
            public void resume(ExecutionContext context, Object suspendedState) throws ResolvedControlThrowable
            {
                fn.resume(context, suspendedState);
            }
        }
        
        private class VarArgsFunction extends AbstractFunctionAnyArg
        {
            private final @NonNull Closures.VarArgs fn;
            
            private VarArgsFunction(@NonNull Closures.VarArgs fn) { this.fn = fn; }

            @Override
            public void invoke(ExecutionContext context, Object[] args) throws ResolvedControlThrowable
            {
                context.getReturnBuffer().setTo(fn.invoke(args));
            }

            @Override
            public void resume(ExecutionContext context, Object suspendedState) throws ResolvedControlThrowable
            {
                fn.resume(context, suspendedState);
            }
        }
    }
    
    @UtilityClass
    public class Closures
    {
        private interface AbstractClosure
        {
            default void resume(ExecutionContext context, Object suspendedState) throws ResolvedControlThrowable
            {
                throw new NonsuspendableFunctionException();
            }
        }
        
        @FunctionalInterface
        public interface ZeroArgsVoid extends AbstractClosure
        {
            void invoke();
        }
        
        @FunctionalInterface
        public interface OneArgsVoid<T> extends AbstractClosure
        {
            void invoke(T arg);
        }
        
        @FunctionalInterface
        public interface TwoArgsVoid<T1, T2> extends AbstractClosure
        {
            void invoke(T1 arg1, T2 arg2);
        }
        
        @FunctionalInterface
        public interface ThreeArgsVoid<T1, T2, T3> extends AbstractClosure
        {
            void invoke(T1 arg1, T2 arg2, T3 arg3);
        }
        
        @FunctionalInterface
        public interface FourArgsVoid<T1, T2, T3, T4> extends AbstractClosure
        {
            void invoke(T1 arg1, T2 arg2, T3 arg3, T4 arg4);
        }
        
        @FunctionalInterface
        public interface VarArgsVoid extends AbstractClosure
        {
            void invoke(Object[] args);
        }
        
        @FunctionalInterface
        public interface ZeroArgs extends AbstractClosure
        {
            Object invoke();
        }
        
        @FunctionalInterface
        public interface OneArgs<T> extends AbstractClosure
        {
            Object invoke(T arg);
        }
        
        @FunctionalInterface
        public interface TwoArgs<T1, T2> extends AbstractClosure
        {
            Object invoke(T1 arg1, T2 arg2);
        }
        
        @FunctionalInterface
        public interface ThreeArgs<T1, T2, T3> extends AbstractClosure
        {
            Object invoke(T1 arg1, T2 arg2, T3 arg3);
        }
        
        @FunctionalInterface
        public interface FourArgs<T1, T2, T3, T4> extends AbstractClosure
        {
            Object invoke(T1 arg1, T2 arg2, T3 arg3, T4 arg4);
        }
        
        @FunctionalInterface
        public interface VarArgs extends AbstractClosure
        {
            Object invoke(Object[] args);
        }
    }
}
