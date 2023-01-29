#!/usr/bin/env python
from setuptools import find_packages, setup


setup(
    name="clossyne",
    description="Python client for the Clossyne key-value cloud storage database named after the greek goddess of memory.",
    long_description=open("README.md").read().strip(),
    long_description_content_type="text/markdown",
    keywords=["Clossyne", "key-value", "database"],
    license="MIT",
    version="0.0.1",
    packages=find_packages(
        include=[
            "clossyne",
        ]
    ),
    classifiers=[
        "Programming Language :: Python :: 3",
        "License :: OSI Approved :: MIT License",
        "Operating System :: OS Independent",
    ],
    python_requires='>=3.6',
    url="https://github.com/chrisonntag/clossyne-py",
    project_urls={
        "Documentation": "https://github.com/chrisonntag/clossyne-py/blob/master/README.md",
        "Changes": "https://github.com/chrisonntag/clossyne-py/releases",
        "Code": "https://github.com/chrisonntag/clossyne-py",
        "Issue tracker": "https://github.com/chrisonntag/clossyne-py",
    },
    author="Christoph Sonntag",
)

