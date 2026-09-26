import { useCallback, useState } from 'react';

export type BreathState = 'idle' | 'inhale' | 'exhale';

export function useBreathing() {
  const [state, setState] = useState<BreathState>('idle');
  const [breaths, setBreaths] = useState(0);

  const press = useCallback(() => {
    if (typeof navigator !== 'undefined' && 'vibrate' in navigator) {
      navigator.vibrate(15);
    }
    setState((current) => {
      if (current === 'inhale') {
        setBreaths((b) => b + 1);
        return 'exhale';
      }
      return 'inhale';
    });
  }, []);

  return { state, breaths, press };
}