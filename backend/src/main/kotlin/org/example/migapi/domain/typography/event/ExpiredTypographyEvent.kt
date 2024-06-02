package org.example.migapi.domain.typography.event

import org.example.migapi.domain.typography.model.TypographyAndRest
import org.springframework.context.ApplicationEvent

class ExpiredTypographyEvent(source: Any, val typographyAndRest: TypographyAndRest) : ApplicationEvent(source)