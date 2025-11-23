from beyondpythonframework.tracing import init_tracing, get_tracer

from otpextractrights.configuration import Logger
from otpextractrights.execute_job import main


def run():
    Logger.init_logger()
    init_tracing()
    tracer = get_tracer()
    # Start a new trace and set it as the current trace
    with tracer.start_as_current_span("otp-extract-rights-job"):
        main()

if __name__ == '__main__':
    run()