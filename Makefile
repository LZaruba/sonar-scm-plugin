SHELL := bash
REPO_NAME ?= $(notdir $(abspath $(dir $(lastword $(MAKEFILE_LIST)))/..))/$(notdir $(CURDIR))


.PHONY: all
all:
	echo $(REPO_NAME)


.PHONY: build
build:
	docker build -t $(REPO_NAME)  .
