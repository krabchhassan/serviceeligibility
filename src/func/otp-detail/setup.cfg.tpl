[metadata]
{{#app}}
name = {{name}}
description = Otp Beneficiary searching function
long_description_content_type="text/markdown"
long_description=README
author = Team Green - Cegedim-activ
classifiers =
    Operating System :: OS Independent
    Programming Language :: Python :: 3.11
version = {{version}}
python_requires = "=={{pythonVersion}}"
{{/app}}

[options]
package_dir =
    =src

packages = find_namespace:

install_requires =
    {{#libs}}
    {{libName}} == {{libVersion}}
    {{/libs}}


[options.packages.find]
where = src

[options.package_data]
* =  *.json, *.conf
