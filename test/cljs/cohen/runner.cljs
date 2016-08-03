(ns cohen.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [cohen.core-test]))

(doo-tests 'cohen.core-test)
