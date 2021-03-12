SHELL := bash
REPO_NAME ?= $(notdir $(abspath $(dir $(lastword $(MAKEFILE_LIST)))/..))/$(notdir $(CURDIR))


.PHONY: all
all:


.PHONY: build
build:
	docker build -t $(REPO_NAME)  .
