Restructure the system properties.

```
arin.rdapbootstrap.match_scheme_on_redirect=FALSE
arin.rdapbootstrap.data_directory=/opt/rdapbootstrap
arin.rdapbootstrap.as_lookup_chain=iana_as,nro_stats,default
arin.rdapbootstrap.ipv4_lookup_chain=iana_ipv4,nro_stats,default
arin.rdapbootstrap.ipv6_lookup_chain=iana_ipv6,nro_stats,defaul
arin.rdapbootstrap.domain_lookup_chain=iana_dns,nro_stats,default
arin.rdapbootstrap.entity_lookup_chain=my_entity,default

arin.rdapbootstrap.lookup.iana_as.format=as_7484
arin.rdapbootstrap.lookup.iana_as.source=url
arin.rdapbootstrap.lookup.iana_as.url=http://data.iana.org/rdap/asn.json
arin.rdapbootstrap.lookup.iana_as.refetch=7200

arin.rdapbootstrap.lookup.iana_ipv4.format=ipv4_7484
arin.rdapbootstrap.lookup.iana_ipv4.source=url
arin.rdapbootstrap.lookup.iana_ipv4.url=http://data.iana.org/rdap/ipv4.json
arin.rdapbootstrap.lookup.iana_ipv4.refetch=7200

arin.rdapbootstrap.lookup.iana_ipv6.format=ipv6_7484
arin.rdapbootstrap.lookup.iana_ipv6.source=url
arin.rdapbootstrap.lookup.iana_ipv6.url=http://data.iana.org/rdap/ipv6.json
arin.rdapbootstrap.lookup.iana_ipv6.refetch=7200

arin.rdapbootstrap.lookup.iana_domain.format=domain_7484
arin.rdapbootstrap.lookup.iana_domain.source=url
arin.rdapbootstrap.lookup.iana_domain.url=http://data.iana.org/rdap/dns.json
arin.rdapbootstrap.lookup.iana_domain.refetch=7200

arin.rdapbootstrap.lookup.default.format=default_7484
arin.rdapbootstrap.lookup.default.source=file
arin.rdapbootstrap.lookup.default.file=default_bootstrap.json
arin.rdapbootstrap.lookup.default.recheck=300

arin.rdapbootstrap.lookup.my_entity.format=entity_7484
arin.rdapbootstrap.lookup.my_entity.source=file
arin.rdapbootstrap.lookup.my_entity.file=entity_bootstrap.json
arin.rdapbootstrap.lookup.my_entity.recheck=300

arin.rdapbootstrap.lookup.nro_stats.format=rir_stats
arin.rdapbootstrap.lookup.nro_stats.source=url
arin.rdapbootstrap.lookup.nro_stats.url=https://www.nro.net/wp-content/uploads/apnic-uploads/delegated-extended
arin.rdapbootstrap.lookup.nro_stats.refetch=43200
```