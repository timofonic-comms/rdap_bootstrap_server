Restructure the system properties.

```
arin.rdapbootstrap.match_scheme_on_redirect=FALSE
arin.rdapbootstrap.data_directory=/opt/rdapbootstrap

arin.rdapbootstrap.registry_chain.as=iana_as,nro_stats,default
arin.rdapbootstrap.registry_chain.ipv4=iana_ipv4,nro_stats,default
arin.rdapbootstrap.registry_chain.ipv6=iana_ipv6,nro_stats,default
arin.rdapbootstrap.registry_chain.domain=iana_dns,nro_stats,default
arin.rdapbootstrap.registry_chain.entity=my_entity,default

arin.rdapbootstrap.registry.iana_as.type=as_7484
arin.rdapbootstrap.registry.iana_as.source=http://data.iana.org/rdap/asn.json
arin.rdapbootstrap.registry.iana_as.refetch=7200

arin.rdapbootstrap.registry.iana_ipv4.type=ipv4_7484
arin.rdapbootstrap.registry.iana_ipv4.source=http://data.iana.org/rdap/ipv4.json
arin.rdapbootstrap.registry.iana_ipv4.refetch=7200

arin.rdapbootstrap.registry.iana_ipv6.type=ipv6_7484
arin.rdapbootstrap.registry.iana_ipv6.source=http://data.iana.org/rdap/ipv6.json
arin.rdapbootstrap.registry.iana_ipv6.refetch=7200

arin.rdapbootstrap.registry.iana_domain.type=domain_7484
arin.rdapbootstrap.registry.iana_domain.source=http://data.iana.org/rdap/dns.json
arin.rdapbootstrap.registry.iana_domain.refetch=7200

arin.rdapbootstrap.registry.default.type=default_7484
arin.rdapbootstrap.registry.default.source=default_bootstrap.json
arin.rdapbootstrap.registry.default.recheck=300

arin.rdapbootstrap.registry.my_entity.type=entity_7484
arin.rdapbootstrap.registry.my_entity.source=entity_bootstrap.json
arin.rdapbootstrap.registry.my_entity.recheck=300

arin.rdapbootstrap.registry.nro_stats.type=rir_stats
arin.rdapbootstrap.registry.nro_stats.source=https://www.nro.net/wp-content/uploads/apnic-uploads/delegated-extended
arin.rdapbootstrap.registry.nro_stats.refetch=43200
```