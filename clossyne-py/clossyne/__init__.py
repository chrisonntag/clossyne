import os
import sys
import logging
from .core import Clossyne


LOGLEVEL = os.environ.get('CLOSSYNE_LOGLEVEL', 'WARNING').upper()

logger = logging.getLogger(__name__)
logger.setLevel(level=LOGLEVEL)

