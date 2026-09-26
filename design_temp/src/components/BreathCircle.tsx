import React from 'react';
import { AnimatePresence, motion } from 'framer-motion';
import type { BreathState } from '../hooks/useBreathing';

type BreathCircleProps = {
  state: BreathState;
  onPress: () => void;
};

const REST_SCALE = 0.68;
const breathTransition = { duration: 1.4, ease: [0.45, 0, 0.55, 1] as const };

export function BreathCircle({ state, onPress }: BreathCircleProps) {
  const isInhaled = state === 'inhale';
  const scale = isInhaled ? 1 : REST_SCALE;
  const label = isInhaled ? 'Выдох' : 'Вдох';

  return (
    <div className="relative aspect-square w-[min(80vw,420px)]">
      {/* Full-lung boundary guide */}
      <div className="absolute inset-0 rounded-full border border-line" aria-hidden="true" />

      <motion.div
        aria-hidden="true"
        className="absolute inset-0 rounded-full bg-sage-soft/25"
        initial={false}
        animate={{ scale: isInhaled ? 1 : REST_SCALE * 1.1 }}
        transition={breathTransition} />
      

      <motion.button
        type="button"
        onClick={onPress}
        aria-label={label}
        className="absolute inset-0 rounded-full bg-sage outline-none focus-visible:ring-4 focus-visible:ring-sage-soft focus-visible:ring-offset-4 focus-visible:ring-offset-mist"
        initial={false}
        animate={{ scale }}
        whileTap={{ scale: scale * 0.96, transition: { duration: 0.12, ease: 'easeOut' } }}
        transition={breathTransition} />
      

      <div className="pointer-events-none absolute inset-0 flex items-center justify-center">
        <AnimatePresence mode="wait" initial={false}>
          <motion.span
            key={label}
            initial={{ opacity: 0, y: 6 }}
            animate={{ opacity: 1, y: 0 }}
            exit={{ opacity: 0, y: -6 }}
            transition={{ duration: 0.2, ease: [0.23, 1, 0.32, 1] }}
            className="font-serif text-5xl font-medium tracking-tight text-mist sm:text-6xl">
            
            {label}
          </motion.span>
        </AnimatePresence>
      </div>
    </div>);

}